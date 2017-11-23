import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { SmartCard } from './smart-card.model';
import { SmartCardService } from './smart-card.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-smart-card',
    templateUrl: './smart-card.component.html'
})
export class SmartCardComponent implements OnInit, OnDestroy {
smartCards: SmartCard[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private smartCardService: SmartCardService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.smartCardService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.smartCards = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.smartCardService.query().subscribe(
            (res: ResponseWrapper) => {
                this.smartCards = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSmartCards();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SmartCard) {
        return item.id;
    }
    registerChangeInSmartCards() {
        this.eventSubscriber = this.eventManager.subscribe('smartCardListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
