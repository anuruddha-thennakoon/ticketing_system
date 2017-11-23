import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TransportManager } from './transport-manager.model';
import { TransportManagerService } from './transport-manager.service';

@Component({
    selector: 'jhi-transport-manager-detail',
    templateUrl: './transport-manager-detail.component.html'
})
export class TransportManagerDetailComponent implements OnInit, OnDestroy {

    transportManager: TransportManager;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private transportManagerService: TransportManagerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTransportManagers();
    }

    load(id) {
        this.transportManagerService.find(id).subscribe((transportManager) => {
            this.transportManager = transportManager;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransportManagers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'transportManagerListModification',
            (response) => this.load(this.transportManager.id)
        );
    }
}
