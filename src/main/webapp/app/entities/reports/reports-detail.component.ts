import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Reports } from './reports.model';
import { ReportsService } from './reports.service';

@Component({
    selector: 'jhi-reports-detail',
    templateUrl: './reports-detail.component.html'
})
export class ReportsDetailComponent implements OnInit, OnDestroy {

    reports: Reports;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private reportsService: ReportsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReports();
    }

    load(id) {
        this.reportsService.find(id).subscribe((reports) => {
            this.reports = reports;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReports() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reportsListModification',
            (response) => this.load(this.reports.id)
        );
    }
}
