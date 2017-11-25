import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReportsComponent } from './reports.component';
import { ReportsDetailComponent } from './reports-detail.component';
import { ReportsPopupComponent } from './reports-dialog.component';
import { ReportsDeletePopupComponent } from './reports-delete-dialog.component';

export const reportsRoute: Routes = [
    {
        path: 'reports',
        component: ReportsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.reports.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'reports/:id',
        component: ReportsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.reports.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reportsPopupRoute: Routes = [
    {
        path: 'reports-new',
        component: ReportsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.reports.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reports/:id/edit',
        component: ReportsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.reports.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reports/:id/delete',
        component: ReportsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.reports.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
