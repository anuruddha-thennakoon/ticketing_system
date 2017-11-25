import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    ReportsService,
    ReportsPopupService,
    ReportsComponent,
    ReportsDetailComponent,
    ReportsDialogComponent,
    ReportsPopupComponent,
    ReportsDeletePopupComponent,
    ReportsDeleteDialogComponent,
    reportsRoute,
    reportsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...reportsRoute,
    ...reportsPopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReportsComponent,
        ReportsDetailComponent,
        ReportsDialogComponent,
        ReportsDeleteDialogComponent,
        ReportsPopupComponent,
        ReportsDeletePopupComponent,
    ],
    entryComponents: [
        ReportsComponent,
        ReportsDialogComponent,
        ReportsPopupComponent,
        ReportsDeleteDialogComponent,
        ReportsDeletePopupComponent,
    ],
    providers: [
        ReportsService,
        ReportsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemReportsModule {}
