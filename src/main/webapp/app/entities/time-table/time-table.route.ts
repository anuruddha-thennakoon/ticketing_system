import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TimeTableComponent } from './time-table.component';
import { TimeTableDetailComponent } from './time-table-detail.component';
import { TimeTablePopupComponent } from './time-table-dialog.component';
import { TimeTableDeletePopupComponent } from './time-table-delete-dialog.component';

export const timeTableRoute: Routes = [
    {
        path: 'time-table',
        component: TimeTableComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.timeTable.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'time-table/:id',
        component: TimeTableDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.timeTable.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timeTablePopupRoute: Routes = [
    {
        path: 'time-table-new',
        component: TimeTablePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.timeTable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time-table/:id/edit',
        component: TimeTablePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.timeTable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time-table/:id/delete',
        component: TimeTableDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.timeTable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
