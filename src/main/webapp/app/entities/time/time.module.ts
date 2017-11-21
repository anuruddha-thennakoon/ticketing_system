import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    TimeService,
    TimePopupService,
    TimeComponent,
    TimeDetailComponent,
    TimeDialogComponent,
    TimePopupComponent,
    TimeDeletePopupComponent,
    TimeDeleteDialogComponent,
    timeRoute,
    timePopupRoute,
    TimeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...timeRoute,
    ...timePopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TimeComponent,
        TimeDetailComponent,
        TimeDialogComponent,
        TimeDeleteDialogComponent,
        TimePopupComponent,
        TimeDeletePopupComponent,
    ],
    entryComponents: [
        TimeComponent,
        TimeDialogComponent,
        TimePopupComponent,
        TimeDeleteDialogComponent,
        TimeDeletePopupComponent,
    ],
    providers: [
        TimeService,
        TimePopupService,
        TimeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemTimeModule {}
