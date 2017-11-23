/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TicketingSystemTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TransportManagerDetailComponent } from '../../../../../../main/webapp/app/entities/transport-manager/transport-manager-detail.component';
import { TransportManagerService } from '../../../../../../main/webapp/app/entities/transport-manager/transport-manager.service';
import { TransportManager } from '../../../../../../main/webapp/app/entities/transport-manager/transport-manager.model';

describe('Component Tests', () => {

    describe('TransportManager Management Detail Component', () => {
        let comp: TransportManagerDetailComponent;
        let fixture: ComponentFixture<TransportManagerDetailComponent>;
        let service: TransportManagerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TicketingSystemTestModule],
                declarations: [TransportManagerDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TransportManagerService,
                    JhiEventManager
                ]
            }).overrideTemplate(TransportManagerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TransportManagerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransportManagerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TransportManager(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.transportManager).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
