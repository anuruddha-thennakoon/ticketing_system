import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TransportManager } from './transport-manager.model';
import { TransportManagerPopupService } from './transport-manager-popup.service';
import { TransportManagerService } from './transport-manager.service';

@Component({
    selector: 'jhi-transport-manager-delete-dialog',
    templateUrl: './transport-manager-delete-dialog.component.html'
})
export class TransportManagerDeleteDialogComponent {

    transportManager: TransportManager;

    constructor(
        private transportManagerService: TransportManagerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.transportManagerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'transportManagerListModification',
                content: 'Deleted an transportManager'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-transport-manager-delete-popup',
    template: ''
})
export class TransportManagerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private transportManagerPopupService: TransportManagerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.transportManagerPopupService
                .open(TransportManagerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
