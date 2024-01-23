# Preamble
Ferrite interfaces all implement the [General](Documentation#Interfaces#General) interface. If the definitions have an array declaration (`[]`) this means, that an object can have multiple children of this type. If it does not have this declaration, and multiple tags of that type are still added, that counts as undefined behavior.
Everything marked with `~` can also be represented as an attribute instead of a tag. A `?` represents, that a tag does not need to be implemented.
Interfaces can implement other interfaces. This is marked both ways. In the implementee itself under *implementations*, and in the implementer it will be reflected in the textual description. This is left out for the [General](Documentation#Interfaces#General) as it is, as described before implemented by everything.
# Interfaces
## General
All Ferrite interfaces inherit the general interface.
**general::external~** - if the interface is defined elsewhere
- bool - external
If the interface is marked as external all tags besides `<path>` will be ignored. And the string content of the `<path>` tag is used as a path to a XML file containing the interface's actual definition.
**general::path~** - path to the external definition of an interface
- string - path
Only used if *general::external* is true. Shows the path to the xml file containing the interface definition.
**general::query?~** - query for another object
- string - query
If a query tag or a query attribute is found, the behavior of the objects changed, so that all child tags will now modify the queried for object. Leaving a query empty will leave it as a proxy.
The query syntax is as follows: `FROM '[tag type]' GET '[type]'[==/!=/</>]'[alias]'`. This can be stacked further to infinity.
## Versioned
An interface to add versioning to other interfaces. Not meant for use on it's own.
**versioned::ferrite_major~** - major version of Ferrite
- int - major version
**versioned::ferrite_minor~** - minor version of Ferrite
- int - minor version
**versioned::major~** - major version of the interface
- int - major version
**versioned::minor~** - minor version of the interface
- int - minor version
### Implementations
- System
- Machine
## Aliased
An interface adding an alias to an interface. Not meant for use on it's own.
**aliased::alias~** - alias of the interface
- String - alias
### Implementations
- System
- Machine
- State
- Trigger
- Output
## System
Highest level object in Ferrite, combines machines together.
This object is loaded when starting Ferrite via the switch `--system [path here]` or `-s [path here]`.
Implements *Versioned* and *Aliased*

**system::[machine](Documentation#Machine)[]** - undetermined amount of machines
See [Machine](Documentation#Machine) for more Information
**system::[state](Documentation#State)[]** - undetermined amount of states
Mainly used as either proxy or machine origin here.
For more information see [state](Documentation#State).
## Machine
This interface describes a single machine, including it's state machine. 
Implements *Versioned* and *Aliased*

**machine::[trigger](Documentation#Interfaces#Trigger)[]** - undetermined amount of triggers
Triggers are Ferrite's version of inputs/sensors, that also encompass virtual triggering mechanisms like *Timers*.
For more information see [Trigger](Documentation#Interfaces#Trigger).
**machine::[output](Documentation#Interfaces#Output)[]** - undetermined amount of outputs
Outputs are aliased proxies to physical outputs.
For more information see [Output](Documentation#Interfaces#Output).
**machine::[state](Documentation#Interfaces#State)[]** - undetermined amount of states
The states defined in a machine represent it's internal state machine. These are later combined together into one by the [System](Documentation#Interfaces#System).
For more information see [State](Documentation#Interfaces#State).
## State
A state interface describes a state of the state machine. A separate state machine is defined per machine, which is later combined into one by the system.
Implements *Aliased*

**state::origin~** - bool, that represents if a state is the state of origin of a system
- bool - is origin?
The origin value represents if a state should be the origin point of a [System](Documentation#Interfaces#System). This should only ever be true in a [System](Documentation#Interfaces#System).
**state::entry~** - bool, that represents if a state is suitable as an entry to a machine
- bool - is entry?
If this value is true, a system mapping is expected. This state is not expected to be used in a [System](Documentation#Interfaces#System).
**state::begin** - (NOT IMPLEMENTED YET, please ignore) hold state changes to be applied when a state is switched to
Usually either empty, or filled by queried [Outputs](Documentation#Interfaces#Output) and [Triggers](Documentation#Interfaces#Trigger), with the respective values to be modified.
**state::[transition](Documentation#Interfaces#Transition)[]** - undetermined amount of transitions
All possible transitions from the current state.
See [Transition](Documentation#Interfaces#Transition) for more details.
**state::end** - hold state changes to be applied when a state is switched from
Usually either empty, or filled by queried [Outputs](Documentation#Interfaces#Output) and [Triggers](Documentation#Interfaces#Trigger), with the respective values to be modified.
## Transition
A transition is a sub-interface used in States to define transition conditions.

**transition::[state](Documentation#Interfaces#State)** - the state, that is switched to, when the condition is true
- state - the state to switch to
Usually there isn't a full state definition here, but rather a query.
See [State](Documentation#Interfaces#State) for further information.
**transition::if[]** - an undetermined amount of conditions > 0
An if expect an undetermined number(>0) of sub-conditions. Either a `<equals>`, `<not_equals>`, `<greater>` or `<lesser>` tag, that all follow the same formula:
- First comes the trigger to be checked
- Then comes the value to be compared against in a `<value>~` tag
There can be multiple sub-conditions. These are OR'ed together.
If there are multiple conditions, they are AND'ed together.
## Trigger
A trigger is a wrapper for inputs/sensors aswell as virtual triggers.
Implements *Aliased*

**trigger::type~** - declares the type of trigger
- enum - `"digital"` or `"timer"`
An enum to declare the type of trigger.
**trigger::value~** - declares the value of the trigger. This does not need to be initially set and behavior differs based on **trigger::type**
- **digital** - value is not meant to be set manually. It gets updated automatically based on inputs on the controller
- **timer** - on a timer the value represents the time left. So for starting a timer you would set value to the millisecond amount you want to wait, and it will count down. This is most often done the `<begin>` tag in a state
## Output
An output is a representation of a physical output on the controller. It is normally set in `<begin>` and `<end>` tags in a state.

**output::type~** - declares the type of output
- enum - `"digital"` (currently no other types are supported)
An enum to declare the type of trigger.
**output::value~** - declares the value of the output. This does not need to be initially set and behavior differs based on **output::type**
- **digital** - to be set manually in `<begin>` and `<end>`
