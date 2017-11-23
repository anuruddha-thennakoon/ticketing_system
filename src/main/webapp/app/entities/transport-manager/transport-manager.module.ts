import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    TransportManagerService,
    TransportManagerPopupService,
    TransportManagerComponent,
    TransportManagerDetailComponent,
    TransportManagerDialogComponent,
    TransportManagerPopupComponent,
    TransportManagerDeletePopupComponent,
    TransportManagerDeleteDialogComponent,
    transportManagerRoute,
    transportManagerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...transportManagerRoute,
    ...transportManagerPopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TransportManagerComponent,
        TransportManagerDetailComponent,
        TransportManagerDialogComponent,
        TransportManagerDeleteDialogComponent,
        TransportManagerPopupComponent,
        TransportManagerDeletePopupComponent,
    ],
    entryComponents: [
        TransportManagerComponent,
        TransportManagerDialogComponent,
        TransportManagerPopupComponent,
        TransportManagerDeleteDialogComponent,
        TransportManagerDeletePopupComponent,
    ],
    providers: [
        TransportManagerService,
        TransportManagerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemTransportManagerModule {}
