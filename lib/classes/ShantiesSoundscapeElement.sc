/**
 *  @file       ShantiesSoundscapeElement.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Sea shanty samples.
 **/
ShantiesSoundscapeElement : SoundscapeElement {

  var <>bufNames,
    <>bufSections,
    <>filterPatch;

  init {
    arg args;

    super.init(args);

    // send voice through reverb
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -10.0.dbamp()
    );

    this.outChannel.level = -18.0.dbamp();

    this.bufNames = [
      \blowTheManDown,
      \jamboree,
      \cook,
      \talcahuano,
      \milesAway
    ];

    this.bufSections = (
      blowTheManDown: [
        [1.96, 13.98],
        [13.98, 25.82],
        [25.82, 50.79],
        [50.79, 95]
      ],
      jamboree: [
        [0.94, 27],
        [27, 53]
      ],
      cook: [
        [1.47, 14.94],
        [108.08, 131.54]
      ],
      talcahuano: [
        [15.26, 38.39],
        [38.39, 61.88]
      ],
      milesAway: [
        [0.0, 33.95],
        [33.95, 66.81]
      ],
    );

    this.transitionTime = 4.0;

    this.offTimeMin = 10.0;
    this.offTimeMax = 50.0;
  }

  create_next_patch {
    var bufKey,
      bufSection,
      buf,
      sectionDuration;

    super.create_next_patch();

    bufKey = this.bufNames.choose();
    buf = this.bufManager.bufs[bufKey];
    bufSection = this.bufSections[bufKey].choose();
    sectionDuration = bufSection[1] - bufSection[0];
    
    this.onTimeMin = sectionDuration;
    this.onTimeMax = sectionDuration;

    ("preparing shanty: " ++ bufKey /*++ " - " ++ bufSection*/).postln();

    ^Patch("cs.sfx.PlayBufWarped", (
      buf: buf,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      playbackRate: rrand(0.8, 0.9),
      warpAmp: 0.05,
      warpFreq: 0.2,
      //startTime: (buf.duration() - this.onTimeMax).rand(),
      startTime: max(0.0, bufSection[0] - this.transitionTime),
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime,
      pitchDive: 0.5
    ));
  }

}
