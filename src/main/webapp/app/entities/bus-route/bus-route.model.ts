import { BaseEntity } from './../../shared';

export class BusRoute implements BaseEntity {
    constructor(
        public id?: number,
        public routeNumber?: string,
        public description?: string,
        public journeys?: BaseEntity[],
        public timeTables?: BaseEntity[],
    ) {
    }
}
