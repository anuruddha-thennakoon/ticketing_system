import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Recharge } from './recharge.model';
import { RechargeService } from './recharge.service';

@Component({
    selector: 'jhi-recharge-detail',
    templateUrl: './recharge-detail.component.html'
})
export class RechargeDetailComponent implements OnInit, OnDestroy {

    recharge: Recharge;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private rechargeService: RechargeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRecharges();
    }

    load(id) {
        this.rechargeService.find(id).subscribe((recharge) => {
            this.recharge = recharge;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRecharges() {
        this.eventSubscriber = this.eventManager.subscribe(
            'rechargeListModification',
            (response) => this.load(this.recharge.id)
        );
    }
}
