import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Reports } from './reports.model';
import { ReportsService } from './reports.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-reports',
    templateUrl: './reports.component.html'
})
export class ReportsComponent implements OnInit, OnDestroy {
    reports: Reports[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private reportsService: ReportsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.reportsService.search({
                query: this.currentSearch,
            }).subscribe(
                (res: ResponseWrapper) => this.reports = res.json,
                (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.reportsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.reports = res.json;
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
        this.registerChangeInReports();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Reports) {
        return item.id;
    }
    registerChangeInReports() {
        this.eventSubscriber = this.eventManager.subscribe('reportsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    viewReport() {
        console.log('generating report....');
        // this.reportsService.viewReport();
    }
}
