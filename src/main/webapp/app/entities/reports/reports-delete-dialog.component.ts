import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Reports } from './reports.model';
import { ReportsPopupService } from './reports-popup.service';
import { ReportsService } from './reports.service';

@Component({
    selector: 'jhi-reports-delete-dialog',
    templateUrl: './reports-delete-dialog.component.html'
})
export class ReportsDeleteDialogComponent {

    reports: Reports;

    constructor(
        private reportsService: ReportsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reportsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'reportsListModification',
                content: 'Deleted an reports'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reports-delete-popup',
    template: ''
})
export class ReportsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reportsPopupService: ReportsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.reportsPopupService
                .open(ReportsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
