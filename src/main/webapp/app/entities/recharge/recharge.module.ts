import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    RechargeService,
    RechargePopupService,
    RechargeComponent,
    RechargeDetailComponent,
    RechargeDialogComponent,
    RechargePopupComponent,
    RechargeDeletePopupComponent,
    RechargeDeleteDialogComponent,
    rechargeRoute,
    rechargePopupRoute,
} from './';

const ENTITY_STATES = [
    ...rechargeRoute,
    ...rechargePopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RechargeComponent,
        RechargeDetailComponent,
        RechargeDialogComponent,
        RechargeDeleteDialogComponent,
        RechargePopupComponent,
        RechargeDeletePopupComponent,
    ],
    entryComponents: [
        RechargeComponent,
        RechargeDialogComponent,
        RechargePopupComponent,
        RechargeDeleteDialogComponent,
        RechargeDeletePopupComponent,
    ],
    providers: [
        RechargeService,
        RechargePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemRechargeModule {}
