import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    PassengerService,
    PassengerPopupService,
    PassengerComponent,
    PassengerDetailComponent,
    PassengerDialogComponent,
    PassengerPopupComponent,
    PassengerDeletePopupComponent,
    PassengerDeleteDialogComponent,
    passengerRoute,
    passengerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...passengerRoute,
    ...passengerPopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PassengerComponent,
        PassengerDetailComponent,
        PassengerDialogComponent,
        PassengerDeleteDialogComponent,
        PassengerPopupComponent,
        PassengerDeletePopupComponent,
    ],
    entryComponents: [
        PassengerComponent,
        PassengerDialogComponent,
        PassengerPopupComponent,
        PassengerDeleteDialogComponent,
        PassengerDeletePopupComponent,
    ],
    providers: [
        PassengerService,
        PassengerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemPassengerModule {}
