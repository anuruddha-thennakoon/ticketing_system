import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { TimeTable } from './time-table.model';
import { TimeTableService } from './time-table.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-time-table',
    templateUrl: './time-table.component.html'
})
export class TimeTableComponent implements OnInit, OnDestroy {
timeTables: TimeTable[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private timeTableService: TimeTableService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.timeTableService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.timeTables = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.timeTableService.query().subscribe(
            (res: ResponseWrapper) => {
                this.timeTables = res.json;
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
        this.registerChangeInTimeTables();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TimeTable) {
        return item.id;
    }
    registerChangeInTimeTables() {
        this.eventSubscriber = this.eventManager.subscribe('timeTableListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
