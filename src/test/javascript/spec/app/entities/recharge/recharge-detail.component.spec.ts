/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TicketingSystemTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RechargeDetailComponent } from '../../../../../../main/webapp/app/entities/recharge/recharge-detail.component';
import { RechargeService } from '../../../../../../main/webapp/app/entities/recharge/recharge.service';
import { Recharge } from '../../../../../../main/webapp/app/entities/recharge/recharge.model';

describe('Component Tests', () => {

    describe('Recharge Management Detail Component', () => {
        let comp: RechargeDetailComponent;
        let fixture: ComponentFixture<RechargeDetailComponent>;
        let service: RechargeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TicketingSystemTestModule],
                declarations: [RechargeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RechargeService,
                    JhiEventManager
                ]
            }).overrideTemplate(RechargeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RechargeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RechargeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Recharge(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.recharge).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
