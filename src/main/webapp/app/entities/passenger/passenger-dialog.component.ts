import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Passenger } from './passenger.model';
import { PassengerPopupService } from './passenger-popup.service';
import { PassengerService } from './passenger.service';

@Component({
    selector: 'jhi-passenger-dialog',
    templateUrl: './passenger-dialog.component.html'
})
export class PassengerDialogComponent implements OnInit {

    passenger: Passenger;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private passengerService: PassengerService,
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
        if (this.passenger.id !== undefined) {
            this.subscribeToSaveResponse(
                this.passengerService.update(this.passenger));
        } else {
            this.subscribeToSaveResponse(
                this.passengerService.create(this.passenger));
        }
    }

    private subscribeToSaveResponse(result: Observable<Passenger>) {
        result.subscribe((res: Passenger) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Passenger) {
        this.eventManager.broadcast({ name: 'passengerListModification', content: 'OK'});
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
    selector: 'jhi-passenger-popup',
    template: ''
})
export class PassengerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private passengerPopupService: PassengerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.passengerPopupService
                    .open(PassengerDialogComponent as Component, params['id']);
            } else {
                this.passengerPopupService
                    .open(PassengerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
