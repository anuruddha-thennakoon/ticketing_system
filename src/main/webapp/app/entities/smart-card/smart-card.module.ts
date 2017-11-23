import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    SmartCardService,
    SmartCardPopupService,
    SmartCardComponent,
    SmartCardDetailComponent,
    SmartCardDialogComponent,
    SmartCardPopupComponent,
    SmartCardDeletePopupComponent,
    SmartCardDeleteDialogComponent,
    smartCardRoute,
    smartCardPopupRoute,
} from './';

const ENTITY_STATES = [
    ...smartCardRoute,
    ...smartCardPopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SmartCardComponent,
        SmartCardDetailComponent,
        SmartCardDialogComponent,
        SmartCardDeleteDialogComponent,
        SmartCardPopupComponent,
        SmartCardDeletePopupComponent,
    ],
    entryComponents: [
        SmartCardComponent,
        SmartCardDialogComponent,
        SmartCardPopupComponent,
        SmartCardDeleteDialogComponent,
        SmartCardDeletePopupComponent,
    ],
    providers: [
        SmartCardService,
        SmartCardPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemSmartCardModule {}
