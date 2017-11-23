import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Recharge } from './recharge.model';
import { RechargePopupService } from './recharge-popup.service';
import { RechargeService } from './recharge.service';
import { SmartCard, SmartCardService } from '../smart-card';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-recharge-dialog',
    templateUrl: './recharge-dialog.component.html'
})
export class RechargeDialogComponent implements OnInit {

    recharge: Recharge;
    isSaving: boolean;

    smartcards: SmartCard[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private rechargeService: RechargeService,
        private smartCardService: SmartCardService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.smartCardService.query()
            .subscribe((res: ResponseWrapper) => { this.smartcards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.recharge.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rechargeService.update(this.recharge));
        } else {
            this.subscribeToSaveResponse(
                this.rechargeService.create(this.recharge));
        }
    }

    private subscribeToSaveResponse(result: Observable<Recharge>) {
        result.subscribe((res: Recharge) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Recharge) {
        this.eventManager.broadcast({ name: 'rechargeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSmartCardById(index: number, item: SmartCard) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-recharge-popup',
    template: ''
})
export class RechargePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rechargePopupService: RechargePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rechargePopupService
                    .open(RechargeDialogComponent as Component, params['id']);
            } else {
                this.rechargePopupService
                    .open(RechargeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
