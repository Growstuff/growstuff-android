# 0004: Designed for low vision, phone first, tablets supported

- Status: Accepted
- Date: 2026-10-24
- Decided by: Brenda Wallace

## Context

The main use is a gardener out in the garden with a phone: bright sun, dirty hands, and often an older phone. Some of our members have low vision. We also want the app to work on tablets, and Brenda has both a phone and a tablet to test on.

## Decision

The designs are **optimised for low vision** and for use outdoors, with the **phone as the primary device** and **tablets supported**.

In practice:

- **A bold look:** thick (4px) black outlines around every block and control, hard-edged boxes, flat blocks of colour, and short bold labels, with actions in capitals. It is inspired by a reference site Brenda likes, and is deliberately a little different from the website.
- **Growstuff's own colours only:** the brown bar (`#413f3b`), beige ground (`#f4f2ef`), the one green (`#57803c`), white and black. Orange is kept for warnings, and the yellow box that says there are job ideas comes from the website.
- **High contrast:** text meets WCAG AA (4.5:1 or better), and colour is never the only way to tell things apart.
- **Large type and large tap targets:** the main controls are at least 56px tall.
- **Little typing:** pickers and recent items first.
- **Phone first, adaptive:** screens are designed for one column on a phone and become two panes on a tablet. We do not lock orientation.
- **A bit of garden whimsy:** the snail, sprouts, flowers bursting out when a job is ticked off, and lines like "The snail is impressed".

The design system is Growstuff's, kept in Claude Design, and the screens so far are drafted there: sign in, home, jobs, my gardens, a garden, and record a harvest.

## Consequences

- Every screen is checked against these rules, as part of the quality and accessibility epic (#10). Android font scaling and TalkBack must work.
- We build the layout with window size classes from the start, so a tablet layout is not a rewrite.
- The look uses plain boxes and borders, so it is straightforward to build in Compose, but it is not stock Material Design, so the theme needs custom shapes, borders and type.
- The tablet and phone designs need to be tried on real devices, including an old phone, so we notice slow screens.
