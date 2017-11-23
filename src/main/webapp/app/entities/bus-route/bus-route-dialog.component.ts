import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BusRoute } from './bus-route.model';
import { BusRoutePopupService } from './bus-route-popup.service';
import { BusRouteService } from './bus-route.service';
import { TimeTable, TimeTableService } from '../time-table';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-bus-route-dialog',
    templateUrl: './bus-route-dialog.component.html'
})
export class BusRouteDialogComponent implements OnInit {

    busRoute: BusRoute;
    isSaving: boolean;

    timetables: TimeTable[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private busRouteService: BusRouteService,
        private timeTableService: TimeTableService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.timeTableService.query()
            .subscribe((res: ResponseWrapper) => { this.timetables = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.busRoute.id !== undefined) {
            this.subscribeToSaveResponse(
                this.busRouteService.update(this.busRoute));
        } else {
            this.subscribeToSaveResponse(
                this.busRouteService.create(this.busRoute));
        }
    }

    private subscribeToSaveResponse(result: Observable<BusRoute>) {
        result.subscribe((res: BusRoute) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: BusRoute) {
        this.eventManager.broadcast({ name: 'busRouteListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTimeTableById(index: number, item: TimeTable) {
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
    selector: 'jhi-bus-route-popup',
    template: ''
})
export class BusRoutePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private busRoutePopupService: BusRoutePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.busRoutePopupService
                    .open(BusRouteDialogComponent as Component, params['id']);
            } else {
                this.busRoutePopupService
                    .open(BusRouteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
