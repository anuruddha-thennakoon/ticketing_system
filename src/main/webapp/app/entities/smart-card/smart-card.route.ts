import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SmartCardComponent } from './smart-card.component';
import { SmartCardDetailComponent } from './smart-card-detail.component';
import { SmartCardPopupComponent } from './smart-card-dialog.component';
import { SmartCardDeletePopupComponent } from './smart-card-delete-dialog.component';

export const smartCardRoute: Routes = [
    {
        path: 'smart-card',
        component: SmartCardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.smartCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'smart-card/:id',
        component: SmartCardDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.smartCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smartCardPopupRoute: Routes = [
    {
        path: 'smart-card-new',
        component: SmartCardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.smartCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'smart-card/:id/edit',
        component: SmartCardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.smartCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'smart-card/:id/delete',
        component: SmartCardDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ticketingSystemApp.smartCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
