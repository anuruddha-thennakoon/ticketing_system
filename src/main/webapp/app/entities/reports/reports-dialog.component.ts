import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Reports } from './reports.model';
import { ReportsPopupService } from './reports-popup.service';
import { ReportsService } from './reports.service';

@Component({
    selector: 'jhi-reports-dialog',
    templateUrl: './reports-dialog.component.html'
})
export class ReportsDialogComponent implements OnInit {

    reports: Reports;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private reportsService: ReportsService,
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
        if (this.reports.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reportsService.update(this.reports));
        } else {
            this.subscribeToSaveResponse(
                this.reportsService.create(this.reports));
        }
    }

    private subscribeToSaveResponse(result: Observable<Reports>) {
        result.subscribe((res: Reports) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Reports) {
        this.eventManager.broadcast({ name: 'reportsListModification', content: 'OK'});
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
    selector: 'jhi-reports-popup',
    template: ''
})
export class ReportsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reportsPopupService: ReportsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reportsPopupService
                    .open(ReportsDialogComponent as Component, params['id']);
            } else {
                this.reportsPopupService
                    .open(ReportsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
