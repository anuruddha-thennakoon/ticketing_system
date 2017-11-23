import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TicketingSystemSharedModule } from '../../shared';
import {
    BusRouteService,
    BusRoutePopupService,
    BusRouteComponent,
    BusRouteDetailComponent,
    BusRouteDialogComponent,
    BusRoutePopupComponent,
    BusRouteDeletePopupComponent,
    BusRouteDeleteDialogComponent,
    busRouteRoute,
    busRoutePopupRoute,
} from './';

const ENTITY_STATES = [
    ...busRouteRoute,
    ...busRoutePopupRoute,
];

@NgModule({
    imports: [
        TicketingSystemSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BusRouteComponent,
        BusRouteDetailComponent,
        BusRouteDialogComponent,
        BusRouteDeleteDialogComponent,
        BusRoutePopupComponent,
        BusRouteDeletePopupComponent,
    ],
    entryComponents: [
        BusRouteComponent,
        BusRouteDialogComponent,
        BusRoutePopupComponent,
        BusRouteDeleteDialogComponent,
        BusRouteDeletePopupComponent,
    ],
    providers: [
        BusRouteService,
        BusRoutePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemBusRouteModule {}
