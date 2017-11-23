import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TransportManager } from './transport-manager.model';
import { TransportManagerPopupService } from './transport-manager-popup.service';
import { TransportManagerService } from './transport-manager.service';

@Component({
    selector: 'jhi-transport-manager-dialog',
    templateUrl: './transport-manager-dialog.component.html'
})
export class TransportManagerDialogComponent implements OnInit {

    transportManager: TransportManager;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private transportManagerService: TransportManagerService,
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
        if (this.transportManager.id !== undefined) {
            this.subscribeToSaveResponse(
                this.transportManagerService.update(this.transportManager));
        } else {
            this.subscribeToSaveResponse(
                this.transportManagerService.create(this.transportManager));
        }
    }

    private subscribeToSaveResponse(result: Observable<TransportManager>) {
        result.subscribe((res: TransportManager) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TransportManager) {
        this.eventManager.broadcast({ name: 'transportManagerListModification', content: 'OK'});
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
    selector: 'jhi-transport-manager-popup',
    template: ''
})
export class TransportManagerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transportManagerPopupService: TransportManagerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.transportManagerPopupService
                    .open(TransportManagerDialogComponent as Component, params['id']);
            } else {
                this.transportManagerPopupService
                    .open(TransportManagerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
