# Sounds of the Subconscious Seas

A soundscape developed for the All Worlds Fair event that took place in San Francisco during February 2013.  It is written entirely in SuperCollider.

[colin-sullivan.net/main/2013/seas-of-subconscious](http://colin-sullivan.net/main/2013/seas-of-subconscious)

## Dependencies

### cs-supercollider-lib

[github.com/colinsullivan/cs-supercollider-lib](https://github.com/colinsullivan/cs-supercollider-lib)

My general supercollider library which is where I put anything not project-specific.  It can be installed with your other quarks.

### Audio Samples

The audio files used in the soundscape are not being tracked by github.  To download the samples:

```bash
cd seas-of-subconscious/seas-of-subconscious-samples/
./get_samples.sh
```

### Quarks

[dewdrop lib](http://www.dewdrop-world.net/sc3/index.php)

Lots of stuff from here, including heavy use of `MixerChannel`.

cruciallib

`Instr` and `Patch` classes used heavily.

Probably others.

## Running

Once you have the sound files, you can run the soundscape:

```bash
cd seas-of-subconscious/
sclang init.sc
```


