/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TicketingSystemTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BusRouteDetailComponent } from '../../../../../../main/webapp/app/entities/bus-route/bus-route-detail.component';
import { BusRouteService } from '../../../../../../main/webapp/app/entities/bus-route/bus-route.service';
import { BusRoute } from '../../../../../../main/webapp/app/entities/bus-route/bus-route.model';

describe('Component Tests', () => {

    describe('BusRoute Management Detail Component', () => {
        let comp: BusRouteDetailComponent;
        let fixture: ComponentFixture<BusRouteDetailComponent>;
        let service: BusRouteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TicketingSystemTestModule],
                declarations: [BusRouteDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BusRouteService,
                    JhiEventManager
                ]
            }).overrideTemplate(BusRouteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusRouteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusRouteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BusRoute(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.busRoute).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
