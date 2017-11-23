import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SmartCard } from './smart-card.model';
import { SmartCardService } from './smart-card.service';

@Component({
    selector: 'jhi-smart-card-detail',
    templateUrl: './smart-card-detail.component.html'
})
export class SmartCardDetailComponent implements OnInit, OnDestroy {

    smartCard: SmartCard;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private smartCardService: SmartCardService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSmartCards();
    }

    load(id) {
        this.smartCardService.find(id).subscribe((smartCard) => {
            this.smartCard = smartCard;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSmartCards() {
        this.eventSubscriber = this.eventManager.subscribe(
            'smartCardListModification',
            (response) => this.load(this.smartCard.id)
        );
    }
}
