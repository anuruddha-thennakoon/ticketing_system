import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TicketingSystemPassengerModule } from './passenger/passenger.module';
import { TicketingSystemTransportManagerModule } from './transport-manager/transport-manager.module';
import { TicketingSystemTimeTableModule } from './time-table/time-table.module';
import { TicketingSystemSmartCardModule } from './smart-card/smart-card.module';
import { TicketingSystemJourneyModule } from './journey/journey.module';
import { TicketingSystemRechargeModule } from './recharge/recharge.module';
import { TicketingSystemPaymentModule } from './payment/payment.module';
import { TicketingSystemBusRouteModule } from './bus-route/bus-route.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TicketingSystemPassengerModule,
        TicketingSystemTransportManagerModule,
        TicketingSystemTimeTableModule,
        TicketingSystemSmartCardModule,
        TicketingSystemJourneyModule,
        TicketingSystemRechargeModule,
        TicketingSystemPaymentModule,
        TicketingSystemBusRouteModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TicketingSystemEntityModule {}
