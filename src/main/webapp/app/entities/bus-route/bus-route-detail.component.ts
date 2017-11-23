import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { BusRoute } from './bus-route.model';
import { BusRouteService } from './bus-route.service';

@Component({
    selector: 'jhi-bus-route-detail',
    templateUrl: './bus-route-detail.component.html'
})
export class BusRouteDetailComponent implements OnInit, OnDestroy {

    busRoute: BusRoute;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private busRouteService: BusRouteService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBusRoutes();
    }

    load(id) {
        this.busRouteService.find(id).subscribe((busRoute) => {
            this.busRoute = busRoute;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBusRoutes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'busRouteListModification',
            (response) => this.load(this.busRoute.id)
        );
    }
}
