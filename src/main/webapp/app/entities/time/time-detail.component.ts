import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Time } from './time.model';
import { TimeService } from './time.service';

@Component({
    selector: 'jhi-time-detail',
    templateUrl: './time-detail.component.html'
})
export class TimeDetailComponent implements OnInit, OnDestroy {

    time: Time;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private timeService: TimeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTimes();
    }

    load(id) {
        this.timeService.find(id).subscribe((time) => {
            this.time = time;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTimes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'timeListModification',
            (response) => this.load(this.time.id)
        );
    }
}
