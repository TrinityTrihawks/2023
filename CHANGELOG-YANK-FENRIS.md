# Changes Made as Part of Yanking Fenris to 2023 Code

Due to the rather radical structural and framework-related changes
I \(Xavier\) made as part of integrating Fenris into 2023 code, I wrote
the following changelog.

I made all of these changes to allow compile-time switching of subsystem
instances without changing \(as far as possible, obviously some things
don't carry over, like the onboard subsystems\) any commands or
subsystems.

## Drivetrain Abstraction

`Drivetrain` is now an abstract class covering gyroscope and tank-drive
functionalities. Concrete drivetrains \(`BilbotDrivetrain` &
`FenrisDrivetrain`\) now live in their own packages.

## Constant Abstraction

This is the more radical move and was the primary motivator for this
document. Frankly, there was and is no way the disparate set of constants
from this and last years' robot code could/can be practically reconciled.
To facilitate abstraction and, ultimately, "plug-n-play" subsytems, I
yanked `Constants`' subclasses out of it and into those classes/packages
concerned with them. Thus, if a drivetrain has control of its own
constants, the constants themselves become part of the abstraction
and, thus, commands have been removed from the responsibility of knowing
where the constants are coming from \(which responsibility I have always
thought they shouldn't have\).
