import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Recharge } from './recharge.model';
import { RechargePopupService } from './recharge-popup.service';
import { RechargeService } from './recharge.service';

@Component({
    selector: 'jhi-recharge-delete-dialog',
    templateUrl: './recharge-delete-dialog.component.html'
})
export class RechargeDeleteDialogComponent {

    recharge: Recharge;

    constructor(
        private rechargeService: RechargeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rechargeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rechargeListModification',
                content: 'Deleted an recharge'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-recharge-delete-popup',
    template: ''
})
export class RechargeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rechargePopupService: RechargePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rechargePopupService
                .open(RechargeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
