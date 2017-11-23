/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TicketingSystemTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TimeTableDetailComponent } from '../../../../../../main/webapp/app/entities/time-table/time-table-detail.component';
import { TimeTableService } from '../../../../../../main/webapp/app/entities/time-table/time-table.service';
import { TimeTable } from '../../../../../../main/webapp/app/entities/time-table/time-table.model';

describe('Component Tests', () => {

    describe('TimeTable Management Detail Component', () => {
        let comp: TimeTableDetailComponent;
        let fixture: ComponentFixture<TimeTableDetailComponent>;
        let service: TimeTableService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TicketingSystemTestModule],
                declarations: [TimeTableDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TimeTableService,
                    JhiEventManager
                ]
            }).overrideTemplate(TimeTableDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimeTableDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimeTableService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TimeTable(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.timeTable).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
