import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PassengerComponent } from './passenger.component';
import { PassengerDetailComponent } from './passenger-detail.component';
import { PassengerPopupComponent } from './passenger-dialog.component';
import { PassengerDeletePopupComponent } from './passenger-delete-dialog.component';

export const passengerRoute: Routes = [
    {
        path: 'passenger',
        component: PassengerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.passenger.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'passenger/:id',
        component: PassengerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.passenger.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const passengerPopupRoute: Routes = [
    {
        path: 'passenger-new',
        component: PassengerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.passenger.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'passenger/:id/edit',
        component: PassengerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.passenger.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'passenger/:id/delete',
        component: PassengerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.passenger.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
