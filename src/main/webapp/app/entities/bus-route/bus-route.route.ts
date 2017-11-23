import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BusRouteComponent } from './bus-route.component';
import { BusRouteDetailComponent } from './bus-route-detail.component';
import { BusRoutePopupComponent } from './bus-route-dialog.component';
import { BusRouteDeletePopupComponent } from './bus-route-delete-dialog.component';

export const busRouteRoute: Routes = [
    {
        path: 'bus-route',
        component: BusRouteComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.busRoute.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bus-route/:id',
        component: BusRouteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.busRoute.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const busRoutePopupRoute: Routes = [
    {
        path: 'bus-route-new',
        component: BusRoutePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.busRoute.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bus-route/:id/edit',
        component: BusRoutePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.busRoute.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bus-route/:id/delete',
        component: BusRouteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.busRoute.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
