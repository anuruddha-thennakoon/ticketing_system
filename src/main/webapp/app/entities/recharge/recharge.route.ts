import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RechargeComponent } from './recharge.component';
import { RechargeDetailComponent } from './recharge-detail.component';
import { RechargePopupComponent } from './recharge-dialog.component';
import { RechargeDeletePopupComponent } from './recharge-delete-dialog.component';

export const rechargeRoute: Routes = [
    {
        path: 'recharge',
        component: RechargeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.recharge.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'recharge/:id',
        component: RechargeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.recharge.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rechargePopupRoute: Routes = [
    {
        path: 'recharge-new',
        component: RechargePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.recharge.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'recharge/:id/edit',
        component: RechargePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.recharge.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'recharge/:id/delete',
        component: RechargeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.recharge.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
