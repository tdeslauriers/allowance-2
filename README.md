## Allowance

*Micronaut CRUD service to track allowances and perform lookups/updates*
***Note:***
* *Refactor of the [original allowance service](https://github.com/tdeslauriers/allowance) to use Hibernate Reactive.*
* *Orignal was stand-alone, this has been made into a service for the family site.*

**Includes scheduled jobs:**
* update balances
* create daily tasks

**Allowance is age based.  Each week age is divided by total tasks assigned.**
* One increment is deducted from total possible for each incomplete task.
* .5 increment is deducted from total possible for each task completed poorly.  
 

