import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SmartCard } from './smart-card.model';
import { SmartCardPopupService } from './smart-card-popup.service';
import { SmartCardService } from './smart-card.service';

@Component({
    selector: 'jhi-smart-card-delete-dialog',
    templateUrl: './smart-card-delete-dialog.component.html'
})
export class SmartCardDeleteDialogComponent {

    smartCard: SmartCard;

    constructor(
        private smartCardService: SmartCardService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smartCardService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'smartCardListModification',
                content: 'Deleted an smartCard'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smart-card-delete-popup',
    template: ''
})
export class SmartCardDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private smartCardPopupService: SmartCardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.smartCardPopupService
                .open(SmartCardDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
