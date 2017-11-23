import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    TimeTableService,
    TimeTablePopupService,
    TimeTableComponent,
    TimeTableDetailComponent,
    TimeTableDialogComponent,
    TimeTablePopupComponent,
    TimeTableDeletePopupComponent,
    TimeTableDeleteDialogComponent,
    timeTableRoute,
    timeTablePopupRoute,
} from './';

const ENTITY_STATES = [
    ...timeTableRoute,
    ...timeTablePopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TimeTableComponent,
        TimeTableDetailComponent,
        TimeTableDialogComponent,
        TimeTableDeleteDialogComponent,
        TimeTablePopupComponent,
        TimeTableDeletePopupComponent,
    ],
    entryComponents: [
        TimeTableComponent,
        TimeTableDialogComponent,
        TimeTablePopupComponent,
        TimeTableDeleteDialogComponent,
        TimeTableDeletePopupComponent,
    ],
    providers: [
        TimeTableService,
        TimeTablePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemTimeTableModule {}
