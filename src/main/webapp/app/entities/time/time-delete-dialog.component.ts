import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Time } from './time.model';
import { TimePopupService } from './time-popup.service';
import { TimeService } from './time.service';

@Component({
    selector: 'jhi-time-delete-dialog',
    templateUrl: './time-delete-dialog.component.html'
})
export class TimeDeleteDialogComponent {

    time: Time;

    constructor(
        private timeService: TimeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.timeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'timeListModification',
                content: 'Deleted an time'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-time-delete-popup',
    template: ''
})
export class TimeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private timePopupService: TimePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.timePopupService
                .open(TimeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
