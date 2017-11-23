import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TimeTable } from './time-table.model';
import { TimeTablePopupService } from './time-table-popup.service';
import { TimeTableService } from './time-table.service';

@Component({
    selector: 'jhi-time-table-delete-dialog',
    templateUrl: './time-table-delete-dialog.component.html'
})
export class TimeTableDeleteDialogComponent {

    timeTable: TimeTable;

    constructor(
        private timeTableService: TimeTableService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.timeTableService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'timeTableListModification',
                content: 'Deleted an timeTable'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-time-table-delete-popup',
    template: ''
})
export class TimeTableDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timeTablePopupService: TimeTablePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.timeTablePopupService
                .open(TimeTableDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
