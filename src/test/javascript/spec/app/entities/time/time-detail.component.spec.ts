/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TicketingSystemTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TimeDetailComponent } from '../../../../../../main/webapp/app/entities/time/time-detail.component';
import { TimeService } from '../../../../../../main/webapp/app/entities/time/time.service';
import { Time } from '../../../../../../main/webapp/app/entities/time/time.model';

describe('Component Tests', () => {

    describe('Time Management Detail Component', () => {
        let comp: TimeDetailComponent;
        let fixture: ComponentFixture<TimeDetailComponent>;
        let service: TimeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TicketingSystemTestModule],
                declarations: [TimeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TimeService,
                    JhiEventManager
                ]
            }).overrideTemplate(TimeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Time(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.time).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
