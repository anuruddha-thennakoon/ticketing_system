import { BaseEntity } from './../../shared';

export class Passenger implements BaseEntity {
    constructor(
        public id?: number,
        public nic?: string,
        public name?: string,
        public journeys?: BaseEntity[],
    ) {
    }
}
