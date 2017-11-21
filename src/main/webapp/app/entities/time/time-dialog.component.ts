import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Time } from './time.model';
import { TimePopupService } from './time-popup.service';
import { TimeService } from './time.service';

@Component({
    selector: 'jhi-time-dialog',
    templateUrl: './time-dialog.component.html'
})
export class TimeDialogComponent implements OnInit {

    time: Time;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private timeService: TimeService,
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
        if (this.time.id !== undefined) {
            this.subscribeToSaveResponse(
                this.timeService.update(this.time));
        } else {
            this.subscribeToSaveResponse(
                this.timeService.create(this.time));
        }
    }

    private subscribeToSaveResponse(result: Observable<Time>) {
        result.subscribe((res: Time) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Time) {
        this.eventManager.broadcast({ name: 'timeListModification', content: 'OK'});
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
    selector: 'jhi-time-popup',
    template: ''
})
export class TimePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timePopupService: TimePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.timePopupService
                    .open(TimeDialogComponent as Component, params['id']);
            } else {
                this.timePopupService
                    .open(TimeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
