/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TicketingSystemTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SmartCardDetailComponent } from '../../../../../../main/webapp/app/entities/smart-card/smart-card-detail.component';
import { SmartCardService } from '../../../../../../main/webapp/app/entities/smart-card/smart-card.service';
import { SmartCard } from '../../../../../../main/webapp/app/entities/smart-card/smart-card.model';

describe('Component Tests', () => {

    describe('SmartCard Management Detail Component', () => {
        let comp: SmartCardDetailComponent;
        let fixture: ComponentFixture<SmartCardDetailComponent>;
        let service: SmartCardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TicketingSystemTestModule],
                declarations: [SmartCardDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SmartCardService,
                    JhiEventManager
                ]
            }).overrideTemplate(SmartCardDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SmartCardDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmartCardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SmartCard(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.smartCard).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
