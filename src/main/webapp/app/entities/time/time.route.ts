import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TimeComponent } from './time.component';
import { TimeDetailComponent } from './time-detail.component';
import { TimePopupComponent } from './time-dialog.component';
import { TimeDeletePopupComponent } from './time-delete-dialog.component';

@Injectable()
export class TimeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const timeRoute: Routes = [
    {
        path: 'time',
        component: TimeComponent,
        resolve: {
            'pagingParams': TimeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.time.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'time/:id',
        component: TimeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.time.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timePopupRoute: Routes = [
    {
        path: 'time-new',
        component: TimePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.time.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time/:id/edit',
        component: TimePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.time.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'time/:id/delete',
        component: TimeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.time.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
