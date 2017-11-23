import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { TimeTable } from './time-table.model';
import { TimeTableService } from './time-table.service';

@Component({
    selector: 'jhi-time-table-detail',
    templateUrl: './time-table-detail.component.html'
})
export class TimeTableDetailComponent implements OnInit, OnDestroy {

    timeTable: TimeTable;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private timeTableService: TimeTableService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTimeTables();
    }

    load(id) {
        this.timeTableService.find(id).subscribe((timeTable) => {
            this.timeTable = timeTable;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTimeTables() {
        this.eventSubscriber = this.eventManager.subscribe(
            'timeTableListModification',
            (response) => this.load(this.timeTable.id)
        );
    }
}
