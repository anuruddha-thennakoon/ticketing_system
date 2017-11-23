import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TransportManagerComponent } from './transport-manager.component';
import { TransportManagerDetailComponent } from './transport-manager-detail.component';
import { TransportManagerPopupComponent } from './transport-manager-dialog.component';
import { TransportManagerDeletePopupComponent } from './transport-manager-delete-dialog.component';

export const transportManagerRoute: Routes = [
    {
        path: 'transport-manager',
        component: TransportManagerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.transportManager.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'transport-manager/:id',
        component: TransportManagerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.transportManager.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transportManagerPopupRoute: Routes = [
    {
        path: 'transport-manager-new',
        component: TransportManagerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.transportManager.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transport-manager/:id/edit',
        component: TransportManagerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.transportManager.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transport-manager/:id/delete',
        component: TransportManagerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.transportManager.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
