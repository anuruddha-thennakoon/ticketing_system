import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Payment } from './payment.model';
import { PaymentPopupService } from './payment-popup.service';
import { PaymentService } from './payment.service';
import { Journey, JourneyService } from '../journey';
import { SmartCard, SmartCardService } from '../smart-card';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-payment-dialog',
    templateUrl: './payment-dialog.component.html'
})
export class PaymentDialogComponent implements OnInit {

    payment: Payment;
    isSaving: boolean;

    journeys: Journey[];

    smartcards: SmartCard[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private paymentService: PaymentService,
        private journeyService: JourneyService,
        private smartCardService: SmartCardService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.journeyService.query()
            .subscribe((res: ResponseWrapper) => { this.journeys = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.smartCardService.query()
            .subscribe((res: ResponseWrapper) => { this.smartcards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.payment.id !== undefined) {
            this.subscribeToSaveResponse(
                this.paymentService.update(this.payment));
        } else {
            this.subscribeToSaveResponse(
                this.paymentService.create(this.payment));
        }
    }

    private subscribeToSaveResponse(result: Observable<Payment>) {
        result.subscribe((res: Payment) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Payment) {
        this.eventManager.broadcast({ name: 'paymentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackJourneyById(index: number, item: Journey) {
        return item.id;
    }

    trackSmartCardById(index: number, item: SmartCard) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-payment-popup',
    template: ''
})
export class PaymentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private paymentPopupService: PaymentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.paymentPopupService
                    .open(PaymentDialogComponent as Component, params['id']);
            } else {
                this.paymentPopupService
                    .open(PaymentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}