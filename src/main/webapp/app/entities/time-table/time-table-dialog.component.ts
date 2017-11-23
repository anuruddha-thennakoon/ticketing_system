import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TimeTable } from './time-table.model';
import { TimeTablePopupService } from './time-table-popup.service';
import { TimeTableService } from './time-table.service';
import { BusRoute, BusRouteService } from '../bus-route';
import { TransportManager, TransportManagerService } from '../transport-manager';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-time-table-dialog',
    templateUrl: './time-table-dialog.component.html'
})
export class TimeTableDialogComponent implements OnInit {

    timeTable: TimeTable;
    isSaving: boolean;

    busroutes: BusRoute[];

    transportmanagers: TransportManager[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private timeTableService: TimeTableService,
        private busRouteService: BusRouteService,
        private transportManagerService: TransportManagerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.busRouteService.query()
            .subscribe((res: ResponseWrapper) => { this.busroutes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.transportManagerService.query()
            .subscribe((res: ResponseWrapper) => { this.transportmanagers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.timeTable.id !== undefined) {
            this.subscribeToSaveResponse(
                this.timeTableService.update(this.timeTable));
        } else {
            this.subscribeToSaveResponse(
                this.timeTableService.create(this.timeTable));
        }
    }

    private subscribeToSaveResponse(result: Observable<TimeTable>) {
        result.subscribe((res: TimeTable) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: TimeTable) {
        this.eventManager.broadcast({ name: 'timeTableListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBusRouteById(index: number, item: BusRoute) {
        return item.id;
    }

    trackTransportManagerById(index: number, item: TransportManager) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-time-table-popup',
    template: ''
})
export class TimeTablePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timeTablePopupService: TimeTablePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.timeTablePopupService
                    .open(TimeTableDialogComponent as Component, params['id']);
            } else {
                this.timeTablePopupService
                    .open(TimeTableDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
