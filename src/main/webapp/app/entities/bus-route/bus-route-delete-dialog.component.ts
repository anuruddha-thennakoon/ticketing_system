import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BusRoute } from './bus-route.model';
import { BusRoutePopupService } from './bus-route-popup.service';
import { BusRouteService } from './bus-route.service';

@Component({
    selector: 'jhi-bus-route-delete-dialog',
    templateUrl: './bus-route-delete-dialog.component.html'
})
export class BusRouteDeleteDialogComponent {

    busRoute: BusRoute;

    constructor(
        private busRouteService: BusRouteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.busRouteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'busRouteListModification',
                content: 'Deleted an busRoute'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bus-route-delete-popup',
    template: ''
})
export class BusRouteDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private busRoutePopupService: BusRoutePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.busRoutePopupService
                .open(BusRouteDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
