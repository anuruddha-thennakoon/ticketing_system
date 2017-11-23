import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SmartCard } from './smart-card.model';
import { SmartCardPopupService } from './smart-card-popup.service';
import { SmartCardService } from './smart-card.service';

@Component({
    selector: 'jhi-smart-card-dialog',
    templateUrl: './smart-card-dialog.component.html'
})
export class SmartCardDialogComponent implements OnInit {

    smartCard: SmartCard;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private smartCardService: SmartCardService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.smartCard.id !== undefined) {
            this.subscribeToSaveResponse(
                this.smartCardService.update(this.smartCard));
        } else {
            this.subscribeToSaveResponse(
                this.smartCardService.create(this.smartCard));
        }
    }

    private subscribeToSaveResponse(result: Observable<SmartCard>) {
        result.subscribe((res: SmartCard) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SmartCard) {
        this.eventManager.broadcast({ name: 'smartCardListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-smart-card-popup',
    template: ''
})
export class SmartCardPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private smartCardPopupService: SmartCardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.smartCardPopupService
                    .open(SmartCardDialogComponent as Component, params['id']);
            } else {
                this.smartCardPopupService
                    .open(SmartCardDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
