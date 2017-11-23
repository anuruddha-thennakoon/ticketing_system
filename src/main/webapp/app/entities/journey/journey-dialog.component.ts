import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Journey } from './journey.model';
import { JourneyPopupService } from './journey-popup.service';
import { JourneyService } from './journey.service';
import { Payment, PaymentService } from '../payment';
import { Passenger, PassengerService } from '../passenger';
import { SmartCard, SmartCardService } from '../smart-card';
import { BusRoute, BusRouteService } from '../bus-route';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-journey-dialog',
    templateUrl: './journey-dialog.component.html'
})
export class JourneyDialogComponent implements OnInit {

    journey: Journey;
    isSaving: boolean;

    payments: Payment[];

    passengers: Passenger[];

    smartcards: SmartCard[];

    busroutes: BusRoute[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private journeyService: JourneyService,
        private paymentService: PaymentService,
        private passengerService: PassengerService,
        private smartCardService: SmartCardService,
        private busRouteService: BusRouteService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.paymentService
            .query({filter: 'journey-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.journey.payment || !this.journey.payment.id) {
                    this.payments = res.json;
                } else {
                    this.paymentService
                        .find(this.journey.payment.id)
                        .subscribe((subRes: Payment) => {
                            this.payments = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.passengerService.query()
            .subscribe((res: ResponseWrapper) => { this.passengers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.smartCardService.query()
            .subscribe((res: ResponseWrapper) => { this.smartcards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.busRouteService.query()
            .subscribe((res: ResponseWrapper) => { this.busroutes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.journey.id !== undefined) {
            this.subscribeToSaveResponse(
                this.journeyService.update(this.journey));
        } else {
            this.subscribeToSaveResponse(
                this.journeyService.create(this.journey));
        }
    }

    private subscribeToSaveResponse(result: Observable<Journey>) {
        result.subscribe((res: Journey) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Journey) {
        this.eventManager.broadcast({ name: 'journeyListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPaymentById(index: number, item: Payment) {
        return item.id;
    }

    trackPassengerById(index: number, item: Passenger) {
        return item.id;
    }

    trackSmartCardById(index: number, item: SmartCard) {
        return item.id;
    }

    trackBusRouteById(index: number, item: BusRoute) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-journey-popup',
    template: ''
})
export class JourneyPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private journeyPopupService: JourneyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.journeyPopupService
                    .open(JourneyDialogComponent as Component, params['id']);
            } else {
                this.journeyPopupService
                    .open(JourneyDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
